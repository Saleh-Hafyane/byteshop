// Extra reactive-form validators that Angular doesn't ship with.
import {FormControl, ValidationErrors} from "@angular/forms";

// Static validator methods usable in template-driven or reactive forms.
export class CustomValidators {
  // Fails when the input is empty or contains only whitespace.
  static notOnlySpaces(control:FormControl):ValidationErrors{
    if ((control.value!=null)&&(control.value.trim().length===0)){
      return {'notOnlySpaces':true} // error key the template shows a message for
    }else {
      return {};
    }
  }
}
