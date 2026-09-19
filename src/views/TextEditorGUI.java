package views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import models.TextFile;

public class TextEditorGUI extends JFrame
        implements ActionListener {

    private DefaultListModel<TextFile> fileListModel;
    private JList<TextFile> jFileList;

    private JTextArea txtArea;
    private JLabel labelStatus;

    private JMenuItem itemNew;
    private JMenuItem itemRename;
    private JMenuItem itemClear;
    private JMenuItem itemDelete;

    private JButton btnSave;

    private TextFile arquivoAtual;

    public TextEditorGUI() {

        super("Editor de texto");

        setSize(750, 750);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarPainelLateral();
        configurarMenu();
        configurarPainelCentral();
        configurarBarraStatus();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void configurarPainelLateral() {

        fileListModel = new DefaultListModel<>();

        jFileList = new JList<>(fileListModel);
        jFileList.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        jFileList.setCellRenderer(
            new ListCellRenderer<TextFile>() {

                @Override
                public Component getListCellRendererComponent(
                    JList<? extends TextFile> list,
                    TextFile arquivo,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
                ) {

                    JLabel label = new JLabel();

                    if (arquivo != null) {
                        label.setText(arquivo.getName());
                    }

                    label.setFont(list.getFont());
                    label.setBorder(
                        BorderFactory.createEmptyBorder(
                            5,
                            8,
                            5,
                            8
                        )
                    );

                    if (isSelected) {
                        label.setBackground(
                            list.getSelectionBackground()
                        );

                        label.setForeground(
                            list.getSelectionForeground()
                        );

                        label.setOpaque(true);
                    } else {
                        label.setBackground(
                            list.getBackground()
                        );

                        label.setForeground(
                            list.getForeground()
                        );

                        label.setOpaque(false);
                    }

                    return label;
                }
            }
        );

        jFileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                arquivoAtual =
                    jFileList.getSelectedValue();

                if (arquivoAtual != null) {
                    txtArea.setText(
                        arquivoAtual.getContent()
                    );

                    atualizarStatus();
                }
            }
        });

        JPanel painelLateral =
            new JPanel(new BorderLayout());

        painelLateral.setPreferredSize(
            new Dimension(200, 750)
        );

        painelLateral.setBorder(
            BorderFactory.createTitledBorder(
                "Seus arquivos"
            )
        );

        painelLateral.add(
            new JScrollPane(jFileList),
            BorderLayout.CENTER
        );

        add(painelLateral, BorderLayout.WEST);
    }

    private void configurarMenu() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Arquivo");

        itemNew = new JMenuItem("Novo");
        itemRename = new JMenuItem("Renomear");
        itemClear = new JMenuItem("Limpar");
        itemDelete = new JMenuItem("Deletar");

        itemNew.addActionListener(this);
        itemRename.addActionListener(this);
        itemClear.addActionListener(this);
        itemDelete.addActionListener(this);

        menu.add(itemNew);
        menu.add(itemRename);
        menu.add(itemClear);
        menu.add(itemDelete);

        menuBar.add(menu);

        setJMenuBar(menuBar);
    }

    private void configurarPainelCentral() {

        txtArea = new JTextArea();
        txtArea.setFont(
            new Font("Monospaced", Font.PLAIN, 14)
        );
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);

        JScrollPane painelTexto =
            new JScrollPane(txtArea);

        btnSave = new JButton("Salvar");
        btnSave.addActionListener(this);

        JPanel painelCentral =
            new JPanel(new BorderLayout());

        painelCentral.add(
            painelTexto,
            BorderLayout.CENTER
        );

        painelCentral.add(
            btnSave,
            BorderLayout.SOUTH
        );

        painelCentral.setBorder(
            new EmptyBorder(10, 5, 0, 15)
        );

        add(painelCentral, BorderLayout.CENTER);
    }

    private void configurarBarraStatus() {

        JPanel barraStatus =
            new JPanel(new FlowLayout(FlowLayout.LEFT));

        barraStatus.setPreferredSize(
            new Dimension(750, 25)
        );

        labelStatus = new JLabel(
            " Crie ou selecione um arquivo de texto."
        );

        barraStatus.add(labelStatus);

        add(barraStatus, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == itemNew) {
            criarArquivo();

        } else if (e.getSource() == itemRename) {
            renomearArquivo();

        } else if (e.getSource() == itemClear) {
            limparArquivo();

        } else if (e.getSource() == itemDelete) {
            deletarArquivo();

        } else if (e.getSource() == btnSave) {
            salvarArquivo();
        }

        atualizarInterface();
    }

    private void criarArquivo() {

        String nome = JOptionPane.showInputDialog(
            this,
            "Digite o nome do arquivo:"
        );

        if (nome == null) {
            return;
        }

        try {
            TextFile novoArquivo =
                new TextFile(nome);

            fileListModel.addElement(novoArquivo);

            arquivoAtual = novoArquivo;
            txtArea.setText("");

        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void renomearArquivo() {

        if (arquivoAtual == null) {
            mostrarErro(
                "Selecione um arquivo para renomear."
            );
            return;
        }

        String novoNome = JOptionPane.showInputDialog(
            this,
            "Digite o novo nome:",
            arquivoAtual.getName()
        );

        if (novoNome == null) {
            return;
        }

        try {
            arquivoAtual.rename(novoNome);

        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void limparArquivo() {

        if (arquivoAtual == null) {
            mostrarErro(
                "Selecione um arquivo para limpar."
            );
            return;
        }

        arquivoAtual.clear();
        txtArea.setText("");
    }

    private void deletarArquivo() {

        if (arquivoAtual == null) {
            mostrarErro(
                "Selecione um arquivo para deletar."
            );
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente deletar o arquivo?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        fileListModel.removeElement(arquivoAtual);

        if (fileListModel.isEmpty()) {
            arquivoAtual = null;
            txtArea.setText("");
        } else {
            arquivoAtual = fileListModel.getElementAt(0);
            txtArea.setText(
                arquivoAtual.getContent()
            );
        }
    }

    private void salvarArquivo() {

        if (arquivoAtual == null) {

            String nome = JOptionPane.showInputDialog(
                this,
                "Digite o nome do arquivo:"
            );

            if (nome == null) {
                return;
            }

            try {
                TextFile novoArquivo = new TextFile(
                    nome,
                    txtArea.getText()
                );

                fileListModel.addElement(novoArquivo);
                arquivoAtual = novoArquivo;

            } catch (IllegalArgumentException ex) {
                mostrarErro(ex.getMessage());
            }

        } else {
            arquivoAtual.edit(txtArea.getText());
        }
    }

    private void atualizarInterface() {

        if (arquivoAtual == null) {

            labelStatus.setText(
                " Crie ou selecione um arquivo de texto."
            );

            jFileList.clearSelection();

        } else {

            atualizarStatus();

            jFileList.setSelectedValue(
                arquivoAtual,
                true
            );
        }

        jFileList.repaint();
    }

    private void atualizarStatus() {

        if (arquivoAtual != null) {
            labelStatus.setText(
                arquivoAtual.toString()
            );
        }
    }

    private void mostrarErro(String mensagem) {

        JOptionPane.showMessageDialog(
            this,
            mensagem,
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }
}