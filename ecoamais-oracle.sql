-- Script Oracle - API EcoaMais
CREATE SEQUENCE sq_tb_servico     START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE sq_tb_unidade     START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE sq_tb_cidadao     START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE sq_tb_atendimento START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE tb_servico (
    cd_servico      NUMBER(10)     NOT NULL,
    nm_servico      VARCHAR2(150)  NOT NULL,
    ds_servico      VARCHAR2(1000),
    ds_categoria    VARCHAR2(20)   NOT NULL,
    ds_pictograma   VARCHAR2(20)   NOT NULL,
    qt_duracao_min  NUMBER(4)      NOT NULL,
    ds_documentos   VARCHAR2(1000),
    CONSTRAINT pk_tb_servico PRIMARY KEY (cd_servico),
    CONSTRAINT ck_servico_categoria CHECK (ds_categoria IN ('SAUDE', 'DOCUMENTOS', 'BENEFICIOS')),
    CONSTRAINT ck_servico_pictograma CHECK (ds_pictograma IN ('saude', 'vacina', 'documento', 'beneficio')),
    CONSTRAINT ck_servico_duracao CHECK (qt_duracao_min > 0)
);

CREATE TABLE tb_unidade (
    cd_unidade   NUMBER(10)     NOT NULL,
    nm_unidade   VARCHAR2(150)  NOT NULL,
    ds_tipo      VARCHAR2(20)   NOT NULL,
    ds_endereco  VARCHAR2(200)  NOT NULL,
    nm_bairro    VARCHAR2(100)  NOT NULL,
    CONSTRAINT pk_tb_unidade PRIMARY KEY (cd_unidade),
    CONSTRAINT ck_unidade_tipo CHECK (ds_tipo IN ('UBS', 'CRAS', 'POUPATEMPO'))
);

CREATE TABLE tb_cidadao (
    cd_cidadao   NUMBER(10)     NOT NULL,
    nm_cidadao   VARCHAR2(150)  NOT NULL,
    nr_telefone  VARCHAR2(20),
    CONSTRAINT pk_tb_cidadao PRIMARY KEY (cd_cidadao)
);

-- Relação N:N: um serviço é atendido em várias unidades e uma unidade oferece vários serviços
CREATE TABLE tb_servico_unidade (
    cd_servico  NUMBER(10) NOT NULL,
    cd_unidade  NUMBER(10) NOT NULL,
    CONSTRAINT pk_tb_servico_unidade PRIMARY KEY (cd_servico, cd_unidade),
    CONSTRAINT fk_servico_unidade_servico FOREIGN KEY (cd_servico)
        REFERENCES tb_servico (cd_servico) ON DELETE CASCADE,
    CONSTRAINT fk_servico_unidade_unidade FOREIGN KEY (cd_unidade)
        REFERENCES tb_unidade (cd_unidade) ON DELETE CASCADE
);

-- Relações 1:N: um cidadão, um serviço e uma unidade têm vários atendimentos
CREATE TABLE tb_atendimento (
    cd_atendimento  NUMBER(10)    NOT NULL,
    cd_cidadao      NUMBER(10)    NOT NULL,
    cd_servico      NUMBER(10)    NOT NULL,
    cd_unidade      NUMBER(10)    NOT NULL,
    dt_hora         TIMESTAMP     NOT NULL,
    ds_status       VARCHAR2(20)  NOT NULL,
    CONSTRAINT pk_tb_atendimento PRIMARY KEY (cd_atendimento),
    CONSTRAINT fk_atendimento_cidadao FOREIGN KEY (cd_cidadao) REFERENCES tb_cidadao (cd_cidadao),
    CONSTRAINT fk_atendimento_servico FOREIGN KEY (cd_servico) REFERENCES tb_servico (cd_servico),
    CONSTRAINT fk_atendimento_unidade FOREIGN KEY (cd_unidade) REFERENCES tb_unidade (cd_unidade),
    CONSTRAINT ck_atendimento_status CHECK (ds_status IN ('AGENDADO', 'CHAMADO', 'CONCLUIDO', 'CANCELADO'))
);
