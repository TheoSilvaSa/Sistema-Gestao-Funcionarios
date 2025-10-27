import { Component, OnInit, signal, WritableSignal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FuncionarioService } from '../../services/funcionario.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FuncionarioRequest } from '../../models/funcionario.model';
import { Departamento, DepartamentoService } from '../../services/departamento.service';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-funcionario-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterLink,
    InputTextModule, InputNumberModule, CalendarModule, 
    ButtonModule, ToastModule,
    DropdownModule
  ],
  templateUrl: './funcionario-form.component.html',
  providers: [MessageService]
})
export class FuncionarioFormComponent implements OnInit {
  
  funcionarioForm: FormGroup;
  isEditMode = false;
  funcionarioId: number | null = null;
  maxDate = new Date();

  departamentosAtivos: WritableSignal<Departamento[]> = signal([]);

  constructor(
    private fb: FormBuilder,
    private funcionarioService: FuncionarioService,
    private departamentoService: DepartamentoService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {
    this.funcionarioForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      cargo: ['', Validators.required],
      salario: [null, [Validators.required, Validators.min(0.01)]],
      dataAdmissao: [null, Validators.required],
      idDepartamento: [null, Validators.required]
    });
  }

  ngOnInit(): void {
    this.carregarDepartamentosAtivos();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.funcionarioId = +id;
        this.carregarDadosFuncionario(+id);
      }
    });
  }

  carregarDepartamentosAtivos(): void {
    this.departamentoService.getDepartamentosAtivos().subscribe(data => {
      this.departamentosAtivos.set(data);
    });
  }

  carregarDadosFuncionario(id: number): void {
    this.funcionarioService.getFuncionarioById(id).subscribe(data => {
      const dataAdmissao = new Date(data.dataAdmissao + 'T00:00:00-03:00');
      
      this.funcionarioForm.patchValue({
        ...data,
        dataAdmissao: dataAdmissao,
        idDepartamento: data.idDepartamento
      });
    });
  }
  
  get f() { return this.funcionarioForm.controls; }

  onSubmit(): void {
    if (this.funcionarioForm.invalid) {
      this.funcionarioForm.markAllAsTouched();
      return;
    }

    const dataAdmissaoISO = (this.funcionarioForm.value.dataAdmissao as Date)
                              .toISOString().split('T')[0];

    const request: FuncionarioRequest = {
      ...this.funcionarioForm.value,
      dataAdmissao: dataAdmissaoISO
    };

    const action = this.isEditMode 
      ? this.funcionarioService.updateFuncionario(this.funcionarioId!, request)
      : this.funcionarioService.createFuncionario(request);

    action.subscribe({
      next: () => {
        this.messageService.add({ 
          severity: 'success', 
          summary: 'Sucesso', 
          detail: `Funcionário ${this.isEditMode ? 'atualizado' : 'cadastrado'}!` 
        });
        setTimeout(() => this.router.navigate(['/funcionarios']), 1500);
      },
      error: (err) => {
        const erroMsg = err.error?.erro || 'Ocorreu um erro. Tente novamente.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: erroMsg });
      }
    });
  }
}