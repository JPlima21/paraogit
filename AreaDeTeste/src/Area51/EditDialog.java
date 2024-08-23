package Area51;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditDialog extends JDialog {
    private JTextField nomeField;
    private JTextField idadeField;
    private JTextField enderecoField;
    private boolean confirmed;

    public EditDialog(JFrame parent, String id, String nome, String idade, String endereco) {
        super(parent, "Editar Dados", true);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Nome:"));
        nomeField = new JTextField(nome);
        add(nomeField);

        add(new JLabel("Idade:"));
        idadeField = new JTextField(idade);
        add(idadeField);

        add(new JLabel("Endereço:"));
        enderecoField = new JTextField(endereco);
        add(enderecoField);

        JButton confirmButton = new JButton("Confirmar");
        JButton cancelButton = new JButton("Cancelar");
        add(confirmButton);
        add(cancelButton);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getNome() {
        return nomeField.getText();
    }

    public String getIdade() {
        return idadeField.getText();
    }

    public String getEndereco() {
        return enderecoField.getText();
    }
}