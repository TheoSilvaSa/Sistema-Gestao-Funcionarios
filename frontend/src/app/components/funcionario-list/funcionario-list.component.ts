import { Component, OnInit, signal, WritableSignal } from '@angular/core';
import { FuncionarioService } from '../../services/funcionario.service';
import { Funcionario } from '../../models/funcionario.model';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-funcionario-list',
  standalone: true,
  imports: [
    CommonModule, RouterLink, FormsModule,
    TableModule, ButtonModule, TagModule, InputTextModule, 
    SelectButtonModule, ConfirmDialogModule, ToastModule
  ],
  templateUrl: './funcionario-list.component.html',
  providers: [ConfirmationService, MessageService]
})
export class FuncionarioListComponent implements OnInit {
  
  funcionarios: WritableSignal<Funcionario[]> = signal([]);
  isLoading = signal(false);

  filtroCargo: string = '';
  filtroStatus: any = null;
  statusOptions = [
    { label: 'Todos', value: null },
    { label: 'Ativos', value: true },
    { label: 'Inativos', value: false }
  ];

  constructor(
    private funcionarioService: FuncionarioService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.carregarFuncionarios();
  }

  carregarFuncionarios(): void {
    this.isLoading.set(true);
    const cargo = this.filtroCargo.trim() ? this.filtroCargo.trim() : undefined;
    const ativo = this.filtroStatus !== null ? this.filtroStatus.value : undefined;

    this.funcionarioService.getFuncionarios(cargo, ativo).subscribe({
      next: (data) => {
        this.funcionarios.set(data);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  editarFuncionario(id: number): void {
    this.router.navigate(['/funcionarios/editar', id]);
  }

  inativarFuncionario(id: number): void {
    this.confirmationService.confirm({
      message: 'Tem certeza que deseja inativar este funcionário?',
      acceptLabel: 'Sim, inativar',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.funcionarioService.inativarFuncionario(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Funcionário inativado' });
            this.carregarFuncionarios();
          },
          error: (err) => this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.message })
        });
      }
    });
  }
}