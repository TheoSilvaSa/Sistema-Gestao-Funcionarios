import { CanDeactivateFn } from '@angular/router';
import { FormGroup } from '@angular/forms';

export interface CanDeactivateComponent {
  form: FormGroup;
  hasUnsavedChanges(): boolean;
}

export const unsavedChangesGuard: CanDeactivateFn<CanDeactivateComponent> = (component, currentRoute, currentState, nextState) => {
  
  if (!component.hasUnsavedChanges()) {
    return true;
  }

  return confirm('Você possui alterações não salvas. Deseja realmente sair?');
};