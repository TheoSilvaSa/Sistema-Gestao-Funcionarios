import { Routes } from '@angular/router';
import { FuncionarioListComponent } from './components/funcionario-list/funcionario-list.component';
import { FuncionarioFormComponent } from './components/funcionario-form/funcionario-form.component';

import { DepartamentoListComponent } from './components/departamento-list/departamento-list.component';
import { DepartamentoFormComponent } from './components/departamento-form/departamento-form.component';

export const routes: Routes = [
    { path: '', redirectTo: 'funcionarios', pathMatch: 'full' },
    
    { path: 'funcionarios', component: FuncionarioListComponent },
    { path: 'funcionarios/novo', component: FuncionarioFormComponent },
    { path: 'funcionarios/editar/:id', component: FuncionarioFormComponent },
    
    { path: 'departamentos', component: DepartamentoListComponent },
    { path: 'departamentos/novo', component: DepartamentoFormComponent },
    { path: 'departamentos/editar/:id', component: DepartamentoFormComponent }
];