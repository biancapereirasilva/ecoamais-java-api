INSERT INTO tb_servico (cd_servico, nm_servico, ds_servico, ds_categoria, ds_pictograma, qt_duracao_min, ds_documentos)
VALUES (sq_tb_servico.nextval, 'Consulta de rotina', 'Consulta com médico clínico geral.', 'SAUDE', 'saude', 20,
        'RG;Cartão do SUS;Comprovante de residência');

INSERT INTO tb_servico (cd_servico, nm_servico, ds_servico, ds_categoria, ds_pictograma, qt_duracao_min, ds_documentos)
VALUES (sq_tb_servico.nextval, 'Vacinação', 'Aplicação das vacinas do calendário de vacinação.', 'SAUDE', 'vacina', 15,
        'RG;Cartão de vacinação;Cartão do SUS');

INSERT INTO tb_servico (cd_servico, nm_servico, ds_servico, ds_categoria, ds_pictograma, qt_duracao_min, ds_documentos)
VALUES (sq_tb_servico.nextval, 'Segunda via de RG', 'Pedido de nova via do documento de identidade.', 'DOCUMENTOS', 'documento', 30,
        'Certidão de nascimento ou casamento;Comprovante de residência');

INSERT INTO tb_servico (cd_servico, nm_servico, ds_servico, ds_categoria, ds_pictograma, qt_duracao_min, ds_documentos)
VALUES (sq_tb_servico.nextval, 'Cadastro no CadÚnico', 'Cadastro para receber benefícios sociais.', 'BENEFICIOS', 'beneficio', 40,
        'RG;CPF;Comprovante de residência');

INSERT INTO tb_unidade (cd_unidade, nm_unidade, ds_tipo, ds_endereco, nm_bairro)
VALUES (sq_tb_unidade.nextval, 'UBS Vila Mariana', 'UBS', 'Rua Domingos de Morais, 1000', 'Vila Mariana');

INSERT INTO tb_unidade (cd_unidade, nm_unidade, ds_tipo, ds_endereco, nm_bairro)
VALUES (sq_tb_unidade.nextval, 'CRAS Exemplo', 'CRAS', 'Rua Exemplo, 200', 'Centro');

INSERT INTO tb_unidade (cd_unidade, nm_unidade, ds_tipo, ds_endereco, nm_bairro)
VALUES (sq_tb_unidade.nextval, 'Poupatempo Exemplo', 'POUPATEMPO', 'Praça Exemplo, s/n', 'Centro');

-- Quais serviços cada unidade oferece (tabela N:N)
INSERT INTO tb_servico_unidade (cd_servico, cd_unidade)
SELECT s.cd_servico, u.cd_unidade FROM tb_servico s, tb_unidade u
WHERE s.nm_servico IN ('Consulta de rotina', 'Vacinação') AND u.nm_unidade = 'UBS Vila Mariana';

INSERT INTO tb_servico_unidade (cd_servico, cd_unidade)
SELECT s.cd_servico, u.cd_unidade FROM tb_servico s, tb_unidade u
WHERE s.nm_servico = 'Segunda via de RG' AND u.nm_unidade = 'Poupatempo Exemplo';

INSERT INTO tb_servico_unidade (cd_servico, cd_unidade)
SELECT s.cd_servico, u.cd_unidade FROM tb_servico s, tb_unidade u
WHERE s.nm_servico = 'Cadastro no CadÚnico' AND u.nm_unidade = 'CRAS Exemplo';

COMMIT;
