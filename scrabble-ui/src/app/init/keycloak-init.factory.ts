import {KeycloakEventType, KeycloakService} from "keycloak-angular";

export function initializeKeycloak(
  keycloak: KeycloakService
) {
  return async () => {
    keycloak.keycloakEvents$.subscribe({
      next: (e) => {
        if (e.type == KeycloakEventType.OnTokenExpired) {
          console.log("Refreshing token")
          keycloak.updateToken(20);
        }
      }
    });

    return keycloak.init({
      config: {
        url: 'http://localhost:8086',
        realm: 'scrabble',
        clientId: 'scrabble'
      },
      initOptions: {
        checkLoginIframe: false,
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
      }
    });
  }
}
