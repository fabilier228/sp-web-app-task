import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { phoneContact } from '../dto/phone-contact.dto';

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private readonly apiUrl = 'http://localhost:8080/api/contacts';
  private readonly http: HttpClient = inject(HttpClient);

  public sendPrompt(prompt: string): Observable<{contact?:phoneContact}> {
    return this.http.post<{contact?:phoneContact}>(`${this.apiUrl}/command`, { prompt });
  }

  public getAllContacts(): Observable<phoneContact[]> {
    return this.http.get<phoneContact[]>(`${this.apiUrl}/all`);
  }
}
