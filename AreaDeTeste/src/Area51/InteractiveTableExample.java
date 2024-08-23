package Area51;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class InteractiveTableExample extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton editButton;
    private JButton deleteButton;

    public InteractiveTableExample() {
        // Inicializa o modelo da tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Age"}, 0);
        table = new JTable(tableModel);

        // Adiciona algumas linhas de exemplo
        tableModel.addRow(new Object[]{1, "John Doe", 30});
        tableModel.addRow(new Object[]{2, "Jane Smith", 25});

        // Configura o botão de editar
        editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedRow();
            }
        });

        // Configura o botão de excluir
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });

        // Configura o painel e o layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        setTitle("Interactive Table Example");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void editSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Recupera os dados da linha selecionada
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            int age = (Integer) tableModel.getValueAt(selectedRow, 2);

            // Exibe caixas de diálogo para editar os dados
            String newName = JOptionPane.showInputDialog(this, "Edit name:", name);
            if (newName != null && !newName.trim().isEmpty()) {
                String ageInput = JOptionPane.showInputDialog(this, "Edit age:", age);
                try {
                    int newAge = Integer.parseInt(ageInput);
                    // Atualiza os dados na tabela
                    tableModel.setValueAt(newName, selectedRow, 1);
                    tableModel.setValueAt(newAge, selectedRow, 2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid age format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this row?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InteractiveTableExample().setVisible(true));
    }
}

