INSERT INTO projeto
(referencia_projeto, empresa, objeto, descricao, coordenador, valor, data_inicio, data_termino, proposta_relatorios, contratos, artigos, situacao, id_adm)
VALUES
('Ref123', 'Empresa XYZ', 'Desenvolvimento de API', 'Projeto de desenvolvimento de API para gerenciamento de dados', 'João Silva', 100000.00, '2024-09-10', '2024-12-10', 'Relatório mensal', 'Contrato padrão', 'Artigo técnico', 1.0, 1);

INSERT INTO adm
(nome, email, cpf, telefone, senha, tipo, data_cadastro, ativo)
VALUES
('ADM', 'adm@adm.com', '123.456.789-00', '123456789', '$2a$12$1t2yMp05VKXdD.zYw/Jsx.y5PqOONr3Qcj6NnfPg5rUduz/1WQ0Q2', '1', '2024-09-10', 'true');

-- Login:
-- E-mail: adm@adm.com
-- Password: 12345678