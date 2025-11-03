import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { FuncionarioListComponent } from './components/funcionario-list/funcionario-list.component';
import { FuncionarioFormComponent } from './components/funcionario-form/funcionario-form.component';
import { DepartamentoListComponent } from './components/departamento-list/departamento-list.component';
import { DepartamentoFormComponent } from './components/departamento-form/departamento-form.component';
import { authGuard } from './guards/auth.guard';
import { unsavedChangesGuard } from './guards/unsaved-changes.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },

  { 
    path: 'funcionarios', 
    component: FuncionarioListComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'funcionarios/novo', 
    component: FuncionarioFormComponent,
    canActivate: [authGuard],
    canDeactivate: [unsavedChangesGuard]
  },
  { 
    path: 'funcionarios/editar/:id', 
    component: FuncionarioFormComponent,
    canActivate: [authGuard],
    canDeactivate: [unsavedChangesGuard]
  },

  { 
    path: 'departamentos', 
    component: DepartamentoListComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'departamentos/novo', 
    component: DepartamentoFormComponent,
    canActivate: [authGuard],
    canDeactivate: [unsavedChangesGuard]
  },
  { 
    path: 'departamentos/editar/:id', 
    component: DepartamentoFormComponent,
    canActivate: [authGuard],
    canDeactivate: [unsavedChangesGuard]
  },

  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];