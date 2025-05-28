package empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import Util.Conexao;

/**
 * Classe principal que implementa as operações de CRUD (Create, Read, Update, Delete)
 * para as entidades Projeto, Pessoa e Funcionário em um sistema simples de gerenciamento.
 */
public class Principal {

    public static void main(String[] args) throws Exception {
        // Scanner para entrada de dados via terminal
        Scanner sc = new Scanner(System.in);
        // Conexão com o banco de dados
        Connection con = Conexao.conectar();

        int escolhaDeAcao;
        // Laço principal do sistema: apresenta o menu e executa ações até a opção de sair (5) ser escolhida
        do {
            menu();
            escolhaDeAcao = sc.nextInt();
            sc.nextLine(); // Consumir a quebra de linha pendente

            switch (escolhaDeAcao) {
                case 1: // Inserir registros no banco
                    opcaoClasse(); // Exibe opções de classe para inserir
                    int opcaoInsert = sc.nextInt();
                    sc.nextLine();

                    if (opcaoInsert == 1) { // Inserir Projeto
                        System.out.print("Nome do projeto: ");
                        String nome = sc.nextLine();
                        System.out.print("Descrição: ");
                        String descricao = sc.nextLine();
                        System.out.print("ID do funcionário: ");
                        int idFuncionario = sc.nextInt();
                        sc.nextLine();

                        // Verifica se o funcionário existe antes de associar ao projeto
                        if (existeFuncionario(con, idFuncionario)) {
                            PreparedStatement stm = con.prepareStatement(
                                "INSERT INTO projeto (nome, descricao, id_funcionario) VALUES (?, ?, ?)");
                            stm.setString(1, nome);
                            stm.setString(2, descricao);
                            stm.setInt(3, idFuncionario);
                            stm.executeUpdate();
                            System.out.println("Projeto inserido com sucesso!");
                        } else {
                            System.out.println("Erro: funcionário não encontrado.");
                        }

                    } else if (opcaoInsert == 2) { // Inserir Pessoa
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        PreparedStatement stm = con.prepareStatement(
                            "INSERT INTO pessoa (nome, email) VALUES (?, ?)");
                        stm.setString(1, nome);
                        stm.setString(2, email);
                        stm.executeUpdate();
                        System.out.println("Pessoa inserida com sucesso!");

                    } else if (opcaoInsert == 3) { // Inserir Funcionário
                        System.out.print("ID da pessoa: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Matrícula (Fxxx): ");
                        String matricula = sc.nextLine();
                        System.out.print("Departamento: ");
                        String departamento = sc.nextLine();

                        // Verifica se a pessoa existe antes de cadastrá-la como funcionário
                        if (existePessoa(con, id)) {
                            PreparedStatement stm = con.prepareStatement(
                                "INSERT INTO funcionario (id, matricula, departamento) VALUES (?, ?, ?)");
                            stm.setInt(1, id);
                            stm.setString(2, matricula);
                            stm.setString(3, departamento);
                            stm.executeUpdate();
                            System.out.println("Funcionário inserido com sucesso!");
                        } else {
                            System.out.println("Erro: pessoa não existe.");
                        }
                    }
                    break;

                case 2: // Deletar registros
                    opcaoClasse();
                    int opcaoDelete = sc.nextInt();
                    sc.nextLine();

                    if (opcaoDelete == 1) { // Deletar Projeto
                        System.out.print("ID do projeto a excluir: ");
                        int idProjeto = sc.nextInt();
                        sc.nextLine();
                        PreparedStatement stmt = con.prepareStatement("DELETE FROM projeto WHERE id = ?");
                        stmt.setInt(1, idProjeto);
                        int res = stmt.executeUpdate();
                        if (res > 0) System.out.println("Projeto excluído com sucesso!");
                        else System.out.println("Projeto não encontrado.");

                    } else if (opcaoDelete == 2) { // Deletar Pessoa
                        System.out.print("ID da pessoa a excluir: ");
                        int idPessoa = sc.nextInt();
                        sc.nextLine();

                        if (existeFuncionario(con, idPessoa)) {
                            System.out.println("Erro: esta pessoa é um funcionário. Exclua o funcionário antes.");
                        } else {
                            PreparedStatement stmt = con.prepareStatement("DELETE FROM pessoa WHERE id = ?");
                            stmt.setInt(1, idPessoa);
                            int res = stmt.executeUpdate();
                            if (res > 0) System.out.println("Pessoa excluída com sucesso!");
                            else System.out.println("Pessoa não encontrada.");
                        }

                    } else if (opcaoDelete == 3) { // Deletar Funcionário
                        System.out.print("ID do funcionário a excluir: ");
                        int id = sc.nextInt();
                        sc.nextLine();

                        if (funcionarioTemProjetos(con, id)) {
                            System.out.println("Erro: funcionário vinculado a projeto.");
                        } else {
                            PreparedStatement stmt = con.prepareStatement("DELETE FROM funcionario WHERE id = ?");
                            stmt.setInt(1, id);
                            int result = stmt.executeUpdate();
                            if (result > 0) System.out.println("Funcionário excluído com sucesso!");
                            else System.out.println("Funcionário não encontrado.");
                        }
                    }
                    break;

                case 3: // Atualizar registros
                    opcaoClasse();
                    int opcaoUpdate = sc.nextInt();
                    sc.nextLine();

                    if (opcaoUpdate == 1) { // Atualizar Projeto
                        System.out.print("ID do projeto: ");
                        int idProjeto = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Novo nome: ");
                        String novoNome = sc.nextLine();
                        System.out.print("Nova descrição: ");
                        String novaDesc = sc.nextLine();
                        System.out.print("Novo ID do funcionário responsável: ");
                        int novoIdFunc = sc.nextInt();
                        sc.nextLine();

                        if (existeFuncionario(con, novoIdFunc)) {
                            PreparedStatement stmt = con.prepareStatement(
                                "UPDATE projeto SET nome = ?, descricao = ?, id_funcionario = ? WHERE id = ?");
                            stmt.setString(1, novoNome);
                            stmt.setString(2, novaDesc);
                            stmt.setInt(3, novoIdFunc);
                            stmt.setInt(4, idProjeto);
                            int res = stmt.executeUpdate();
                            if (res > 0) System.out.println("Projeto atualizado com sucesso!");
                            else System.out.println("Projeto não encontrado.");
                        } else {
                            System.out.println("Erro: funcionário informado não existe.");
                        }

                    } else if (opcaoUpdate == 2) { // Atualizar Pessoa
                        System.out.print("ID da pessoa: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Novo nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Novo email: ");
                        String email = sc.nextLine();

                        PreparedStatement stmt = con.prepareStatement(
                            "UPDATE pessoa SET nome = ?, email = ? WHERE id = ?");
                        stmt.setString(1, nome);
                        stmt.setString(2, email);
                        stmt.setInt(3, id);
                        int res = stmt.executeUpdate();
                        if (res > 0) System.out.println("Pessoa atualizada com sucesso!");
                        else System.out.println("Pessoa não encontrada.");

                    } else if (opcaoUpdate == 3) { // Atualizar Funcionário
                        System.out.print("ID do funcionário: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Nova matrícula (Fxxx): ");
                        String novaMat = sc.nextLine();
                        System.out.print("Novo departamento: ");
                        String novoDepto = sc.nextLine();

                        if (existeFuncionario(con, id)) {
                            PreparedStatement stmt = con.prepareStatement(
                                "UPDATE funcionario SET matricula = ?, departamento = ? WHERE id = ?");
                            stmt.setString(1, novaMat);
                            stmt.setString(2, novoDepto);
                            stmt.setInt(3, id);
                            int res = stmt.executeUpdate();
                            if (res > 0) System.out.println("Funcionário atualizado com sucesso!");
                            else System.out.println("Funcionário não encontrado.");
                        } else {
                            System.out.println("Erro: funcionário não encontrado.");
                        }
                    }
                    break;

                case 4: // Consultar registros (listar todos)
                    opcaoClasse();
                    int opcaoSelect = sc.nextInt();
                    sc.nextLine();

                    if (opcaoSelect == 1) { // Listar Projetos
                        PreparedStatement stm = con.prepareStatement("SELECT * FROM projeto");
                        ResultSet rs = stm.executeQuery();
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id") +
                                    ", Nome: " + rs.getString("nome") +
                                    ", Funcionário: " + rs.getInt("id_funcionario"));
                        }
                    } else if (opcaoSelect == 2) { // Listar Pessoas
                        PreparedStatement stm = con.prepareStatement("SELECT * FROM pessoa");
                        ResultSet rs = stm.executeQuery();
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id") +
                                    ", Nome: " + rs.getString("nome") +
                                    ", Email: " + rs.getString("email"));
                        }
                    } else if (opcaoSelect == 3) { // Listar Funcionários
                        PreparedStatement stm = con.prepareStatement("SELECT * FROM funcionario");
                        ResultSet rs = stm.executeQuery();
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id") +
                                    ", Matrícula: " + rs.getString("matricula") +
                                    ", Departamento: " + rs.getString("departamento"));
                        }
                    }
                    break;

                case 5: // Encerrar o sistema
                    System.out.println("Encerrando sistema...");
                    break;

                default: // Opção inválida
                    System.out.println("Opção inválida.");
            }

        } while (escolhaDeAcao != 5); // Continua até o usuário escolher sair

        sc.close();
        con.close(); // Fecha recursos
    }

    // Exibe o menu principal de ações
    public static void menu() {
        System.out.println("\nSelecione a ação:");
        System.out.println("1 - Inserir");
        System.out.println("2 - Deletar");
        System.out.println("3 - Atualizar");
        System.out.println("4 - Consultar");
        System.out.println("5 - Sair");
    }

    // Exibe as opções de classes (entidades) para escolher
    public static void opcaoClasse() {
        System.out.println("Escolha a classe:");
        System.out.println("1 - Projeto");
        System.out.println("2 - Pessoa");
        System.out.println("3 - Funcionário");
    }

    // Verifica se uma pessoa existe no banco
    public static boolean existePessoa(Connection con, int id) throws Exception {
        PreparedStatement stm = con.prepareStatement("SELECT id FROM pessoa WHERE id = ?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        return rs.next();
    }

    // Verifica se um funcionário existe no banco
    public static boolean existeFuncionario(Connection con, int id) throws Exception {
        PreparedStatement stm = con.prepareStatement("SELECT id FROM funcionario WHERE id = ?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        return rs.next();
    }

    // Verifica se um funcionário possui projetos vinculados
    public static boolean funcionarioTemProjetos(Connection con, int id) throws Exception {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM projeto WHERE id_funcionario = ?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        return rs.next();
    }
}
