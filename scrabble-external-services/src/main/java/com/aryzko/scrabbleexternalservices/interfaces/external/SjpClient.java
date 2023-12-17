package com.aryzko.scrabbleexternalservices.interfaces.external;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class SjpClient implements WordDefinitionClient {

    private final WebClient sjpWebClient;

    @Override
    public Mono<String> getWordDefinition(String word) {
        return sjpWebClient.get()
                .uri(uriBuilder -> uriBuilder.pathSegment(word).build())
                .retrieve()
                .bodyToMono(String.class)
                .map(html -> {
                    Document document = Jsoup.parse(html);
                    Element h1Element = document.selectFirst("h1");
                    Elements paragraphsAfterH1 = new Elements();
                    if (h1Element != null) {
                        Element nextElement = h1Element.nextElementSibling();
                        while (nextElement != null) {
                            if (nextElement.tagName().equals("p")) {
                                paragraphsAfterH1.add(nextElement);
                            }
                            nextElement = nextElement.nextElementSibling();
                        }
                    }
                    String acceptableInGames = getParagraphText(paragraphsAfterH1, 1);
                    String wordDefinition = getParagraphText(paragraphsAfterH1, 3);
                    if(acceptableInGames.contains("dopuszczalne w grach")) {
                        return ofNullable(wordDefinition).orElse("No definition");
                    } else {
                        return "No definition";
                    }
                });
    }

    private static String getParagraphText(Elements paragraphs, int paragraphNumber) {
        String wordDefinition = null;
        if (paragraphs.size() >= paragraphNumber) {
            wordDefinition = paragraphs.get(paragraphNumber-1).text();
        }
        return wordDefinition;
    }

    @Override
    public String supportedLanguageCode() {
        return "pl";
    }
}
