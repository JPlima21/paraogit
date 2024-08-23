package Area51;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class testeBancoDeDados extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public testeBancoDeDados() {
        // Configuração do JFrame
        setTitle("Tabela Postgresql");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Criação do modelo e da tabela
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nome");
        model.addColumn("Idade");
        model.addColumn("Endereço");
        table = new JTable(model);
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane, BorderLayout.CENTER);

        // Adiciona botões
        JPanel panel = new JPanel();
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");
        panel.add(editButton);
        panel.add(deleteButton);
        add(panel, BorderLayout.SOUTH);

        // Configuração da conexão com o PostgreSQL
        String url = "jdbc:postgresql://localhost:5432/Registro_de_leitor";
        String user = "postgres";
        String password = "postgres";

        // Preenche a tabela
        populateTable(url, user, password);

        // Adiciona ação ao botão de edição
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRow(url, user, password);
            }
        });

        // Adiciona ação ao botão de exclusão
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRow(url, user, password);
            }
        });
    }

    private void populateTable(String url, String user, String password) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM registrodeleitor");

            while (rs.next()) {
                String id = rs.getString("id");
                String nome = rs.getString("nome");
                String idade = rs.getString("idade");
                String endereco = rs.getString("endereco");
                model.addRow(new Object[]{id, nome, idade, endereco});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editRow(String url, String user, String password) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String id = model.getValueAt(selectedRow, 0).toString();
            String nome = model.getValueAt(selectedRow, 1).toString();
            String idade = model.getValueAt(selectedRow, 2).toString();
            String endereco = model.getValueAt(selectedRow, 3).toString();

            // Crie uma lógica para editar os dados
            // Neste exemplo, apenas exiba os valores em um diálogo para edição
            EditDialog dialog = new EditDialog(this, id, nome, idade, endereco);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                // Atualize os dados no banco de dados
                try (Connection connection = DriverManager.getConnection(url, user, password)) {
                    String query = "UPDATE registrodeleitor SET nome = ?, idade = ?, endereco = ? WHERE id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                        pstmt.setString(1, dialog.getNome());
                        pstmt.setInt(2, Integer.parseInt(dialog.getIdade()));
                        pstmt.setString(3, dialog.getEndereco());
                        pstmt.setInt(4, Integer.parseInt(id));
                        pstmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Atualize a tabela
                model.setValueAt(dialog.getNome(), selectedRow, 1);
                model.setValueAt(dialog.getIdade(), selectedRow, 2);
                model.setValueAt(dialog.getEndereco(), selectedRow, 3);
            }
        } else {
            System.out.println("Nenhuma linha selecionada para edição.");
        }
    }

    private void deleteRow(String url, String user, String password) {
    	int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String id = model.getValueAt(selectedRow, 0).toString();

            // Exclua os dados do banco de dados
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String query = "DELETE FROM registrodeleitor WHERE id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setInt(1, Integer.parseInt(id));
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Atualize a tabela
            model.removeRow(selectedRow);
        } else {
            System.out.println("Nenhuma linha selecionada para exclusão.");
        }
    }

    public static void main(String[] args) {
        new testeBancoDeDados().setVisible(true);
    }
}
	