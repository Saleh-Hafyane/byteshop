import {FormControl, ValidationErrors} from "@angular/forms";

export class CustomValidators {
  static notOnlySpaces(control:FormControl):ValidationErrors{
    if ((control.value!=null)&&(control.value.trim().length===0)){
      return {'notOnlySpaces':true}
    }else {
      return {};
    }
  }
}
