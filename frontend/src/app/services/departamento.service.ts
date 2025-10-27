import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Departamento {
  id: number;
  nome: string;
  sigla: string;
  ativo: boolean;
}

export interface DepartamentoRequest {
  nome: string;
  sigla: string;
}


@Injectable({
  providedIn: 'root'
})
export class DepartamentoService {
  private readonly apiUrl = 'http://localhost:8080/api/departamentos';

  constructor(private http: HttpClient) { }

  getDepartamentos(): Observable<Departamento[]> {
    return this.http.get<Departamento[]>(this.apiUrl);
  }

  getDepartamentosAtivos(): Observable<Departamento[]> {
    return this.http.get<Departamento[]>(`${this.apiUrl}/ativos`);
  }

  createDepartamento(data: DepartamentoRequest): Observable<Departamento> {
    return this.http.post<Departamento>(this.apiUrl, data);
  }

  updateDepartamento(id: number, data: DepartamentoRequest): Observable<Departamento> {
    return this.http.put<Departamento>(`${this.apiUrl}/${id}`, data);
  }

  inativarDepartamento(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/inativar`, {});
  }

  getDepartamentoById(id: number): Observable<Departamento> {
    return this.http.get<Departamento>(`${this.apiUrl}/${id}`);
  }
}