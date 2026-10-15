// Standalone presentational component: turns the search box into a catalog route.
import { Component } from '@angular/core';
import {Router, Routes} from "@angular/router";
import {routes} from "../../app.routes";

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css',
})
export class SearchComponent {
// Injected Router; the only dependency this component needs.

// Navigates to the catalog search route with the given keyword.

  constructor(private router:Router) {
  }
  search(value: string) {
    this.router.navigateByUrl("/search/"+value)
  }
}
