package com.aryzko.scrabble.scrabbleboardmanager.domain.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.ports.ResolveCommandSender;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResolveCommandMapper {
    List<ResolveCommandSender.LineField> convert(List<PreparedLine.LineField> field);
    ResolveCommandSender.LineField convert(PreparedLine.LineField field);
}
