import { Component, OnInit, signal, WritableSignal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Departamento, DepartamentoService } from '../../services/departamento.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-departamento-list',
  standalone: true,
  imports: [
    CommonModule, RouterLink,
    TableModule, ButtonModule, TagModule,
    ToastModule, ConfirmDialogModule
  ],
  templateUrl: './departamento-list.component.html',
  providers: [MessageService, ConfirmationService]
})
export class DepartamentoListComponent implements OnInit {

  departamentos: WritableSignal<Departamento[]> = signal([]);
  isLoading = signal(false);

  constructor(
    private departamentoService: DepartamentoService,
    private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.carregarDepartamentos();
  }

  carregarDepartamentos(): void {
    this.isLoading.set(true);
    this.departamentoService.getDepartamentos().subscribe({
      next: (data) => {
        this.departamentos.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar departamentos.' });
        this.isLoading.set(false);
      }
    });
  }

  editarDepartamento(id: number): void {
    this.router.navigate(['/departamentos/editar', id]);
  }

  inativarDepartamento(id: number): void {
    this.confirmationService.confirm({
      message: 'Tem certeza que deseja inativar este departamento?',
      acceptLabel: 'Sim, inativar',
      rejectLabel: 'Cancelar',
      accept: () => {
        this.departamentoService.inativarDepartamento(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Departamento inativado.' });
            this.carregarDepartamentos();
          },
          error: (err) => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao inativar departamento.' });
          }
        });
      }
    });
  }
}