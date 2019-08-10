import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PersonalData } from '../classes/User';

@Injectable({
  providedIn: 'root'
})
export class PersonalDataService {

  constructor(private http: HttpClient) { }

  sendPersonalData(personalData: PersonalData) {
    return this.http.post('http://localhost:8090/api/user/naturalperson',
    {
      'firstName': personalData.firstName,
      'apartmentNo': personalData.apartmentNo,
      'buildingNo': personalData.buildingNo,
      'city': personalData.city,
      'lastName': personalData.lastName,
      'phoneNo': personalData.phoneNo,
      'street': personalData.street,
      'voivodeship': personalData.voivodeship
    }
    );
  }
}