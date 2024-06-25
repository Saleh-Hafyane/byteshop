import { Injectable } from '@angular/core';
import {map, Observable, of} from "rxjs";
import {City} from "../common/city";
import {HttpClient} from "@angular/common/http";



@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor(private httpClient:HttpClient) { }
  getCities():Observable<City[]>{
    const citiesUrl:string="http://localhost:8080/api/city"
    return this.httpClient.get<citiesInterface>(citiesUrl).pipe(map(data=>data._embedded.city))
  }
  getMonthsData(startMonth:number):Observable<number[]>{
    const monthsData:number[] = []
    for (let month = startMonth;month<=12;month++){
      monthsData.push(month)
    }
    return of(monthsData)

  }

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
interface citiesInterface {
  _embedded:{
    city:City[]
  }
}
