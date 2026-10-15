// Supplies checkout form data: the city list from the backend and locally built expiry dropdowns.
import { Injectable } from '@angular/core';
import {map, Observable, of} from "rxjs";
import {City} from "../common/city";
import {HttpClient} from "@angular/common/http";



// Provided in root; used by the checkout form only.
@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor(private httpClient:HttpClient) { }

  // GET /api/city and unwrap the Spring Data REST _embedded envelope.
  getCities():Observable<City[]>{
    const citiesUrl:string="http://localhost:8080/api/city"
    return this.httpClient.get<citiesInterface>(citiesUrl).pipe(map(data=>data._embedded.city))
  }

  // Credit-card months from the chosen start month to December, for the expiry dropdown.
  getMonthsData(startMonth:number):Observable<number[]>{
    const monthsData:number[] = []
    for (let month = startMonth;month<=12;month++){
      monthsData.push(month)
    }
    return of(monthsData)

  }

  // Credit-card years from now to ten years ahead, for the expiry dropdown.
  getYearsData():Observable<number[]> {
    const yearsData:number[] = []
    const startYear:number = new Date().getFullYear()
    const lastYear:number = startYear+10
    for (let year = startYear;year<=lastYear;year++){
      yearsData.push(year)
    }
    return of(yearsData)
  }
}
// Shape of the Spring Data REST list response for cities.
interface citiesInterface {
  _embedded:{
    city:City[]
  }
}
