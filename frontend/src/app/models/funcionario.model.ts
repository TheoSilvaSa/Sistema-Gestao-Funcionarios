export interface Funcionario {
  id: number;
  nome: string;
  email: string;
  cargo: string;
  salario: number;
  dataAdmissao: string;
  ativo: boolean;
  idDepartamento: number;
  nomeDepartamento: string;
  ativoDepartamento: boolean;
}

export interface FuncionarioRequest {
  nome: string;
  email: string;
  cargo: string;
  salario: number;
  dataAdmissao: string;
  idDepartamento: number;
}