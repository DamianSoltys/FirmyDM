import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PersonalData } from '../classes/User';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PersonalDataService {
  personalData = new BehaviorSubject(<PersonalData>null);
  
  constructor(private http: HttpClient) { }

  public sendPersonalData(personalData: PersonalData,userId:number) {
    return this.http.post(`http://localhost:8090/api/user/${userId}/naturalperson`,
    {
      'address':{
      'apartmentNo': personalData.address.apartmentNo,
      'buildingNo': personalData.address.buildingNo,
      'street': personalData.address.street,
      'voivodeship': personalData.address.voivodeship,
      'city': personalData.address.city,
      },
      'firstName': personalData.firstName,
      'lastName': personalData.lastName,
      'phoneNo': personalData.phoneNo
    }
    );
  }

  public editPersonalData(personalData: PersonalData,userId:number,naturalPersonId:number) {
    return this.http.patch(`http://localhost:8090/api/user/${userId}/naturalperson/${naturalPersonId}`,
    {
      'address':{
      'apartmentNo': personalData.address.apartmentNo,
      'buildingNo': personalData.address.buildingNo,
      'street': personalData.address.street,
      'voivodeship': personalData.address.voivodeship,
      'city': personalData.address.city,
      },
      'firstName': personalData.firstName,
      'lastName': personalData.lastName,
      'phoneNo': personalData.phoneNo
    }
    );
  }

  public getPersonalData(userId: number,naturalPersonId: number): Observable<any> {
    return this.http.get(`http://localhost:8090/api/user/${userId}/naturalperson/${naturalPersonId}`,{observe: 'response'});
  }

  public deletePersonalData(userId: number,naturalPersonId: number): Observable<any> {
    return this.http.delete(`http://localhost:8090/api/user/${userId}/naturalperson/${naturalPersonId}`,{observe: 'response'});
  }
}
