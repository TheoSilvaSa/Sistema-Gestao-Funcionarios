import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Funcionario, FuncionarioRequest } from '../models/funcionario.model';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {
  private readonly apiUrl = 'http://localhost:8080/api/funcionarios';

  constructor(private http: HttpClient) { }

  getFuncionarios(cargo?: string, ativo?: boolean): Observable<Funcionario[]> {
    let params = new HttpParams();
    if (cargo) {
      params = params.append('cargo', cargo);
    }
    if (ativo !== undefined && ativo !== null) {
      params = params.append('ativo', ativo);
    }
    return this.http.get<Funcionario[]>(this.apiUrl, { params });
  }

  getFuncionarioById(id: number): Observable<Funcionario> {
    return this.http.get<Funcionario>(`${this.apiUrl}/${id}`);
  }

  createFuncionario(data: FuncionarioRequest): Observable<Funcionario> {
    return this.http.post<Funcionario>(this.apiUrl, data);
  }

  updateFuncionario(id: number, data: FuncionarioRequest): Observable<Funcionario> {
    return this.http.put<Funcionario>(`${this.apiUrl}/${id}`, data);
  }

  inativarFuncionario(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/inativar`, {});
  }
}