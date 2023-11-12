import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {KeycloakService} from "keycloak-angular";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private keycloakService: KeycloakService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.keycloakService.getKeycloakInstance().token;
    console.log("AuthInterceptor is triggered, token: " + token);
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request);
  }
}
