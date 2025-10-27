import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { DepartamentoRequest, DepartamentoService } from '../../services/departamento.service';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-departamento-form',
  standalone: true,
  imports: [
    CommonModule, RouterLink, ReactiveFormsModule,
    InputTextModule, ButtonModule, ToastModule
  ],
  templateUrl: './departamento-form.component.html',
  providers: [MessageService]
})
export class DepartamentoFormComponent implements OnInit {

  deptoForm: FormGroup;
  isEditMode = false;
  deptoId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private departamentoService: DepartamentoService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {
    this.deptoForm = this.fb.group({
      nome: ['', [Validators.required]],
      sigla: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.deptoId = +id;
        this.carregarDadosDepto(this.deptoId);
      }
    });
  }

  carregarDadosDepto(id: number): void {
    this.departamentoService.getDepartamentoById(id).subscribe(data => {
      this.deptoForm.patchValue(data);
    });
  }

  get f() { return this.deptoForm.controls; }

  onSubmit(): void {
    if (this.deptoForm.invalid) {
      this.deptoForm.markAllAsTouched();
      return;
    }

    const request: DepartamentoRequest = this.deptoForm.value;

    const action = this.isEditMode
      ? this.departamentoService.updateDepartamento(this.deptoId!, request)
      : this.departamentoService.createDepartamento(request);

    action.subscribe({
      next: () => {
        this.messageService.add({ 
          severity: 'success', 
          summary: 'Sucesso', 
          detail: `Departamento ${this.isEditMode ? 'atualizado' : 'criado'}!` 
        });
        setTimeout(() => this.router.navigate(['/departamentos']), 1500);
      },
      error: (err) => {
        const erroMsg = err.error?.erro || 'Ocorreu um erro.';
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: erroMsg });
      }
    });
  }
}