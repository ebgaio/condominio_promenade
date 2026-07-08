package br.com.edificiopromenade.presentation.condominio.mapper

import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.presentation.condominio.model.CondominioUi

fun CondominioEntity.toUi() =

    CondominioUi(

        id = id,

        nome = nome,

        cnpj = cnpj ?: "",

        endereco = endereco ?: "",

        nomeAdministradora = nomeAdministradora ?: "",

        emailAdministradora = emailAdministradora ?: "",

        ativo = ativo,

        dataCriacao = dataCriacao,

        dataInativacao = dataInativacao
    )

fun CondominioUi.toEntity() =

    CondominioEntity(

        id = id,

        nome = nome,

        cnpj = cnpj,

        endereco = endereco,

        nomeAdministradora = nomeAdministradora,

        emailAdministradora = emailAdministradora,

        ativo = ativo,

        dataCriacao = dataCriacao,

        dataInativacao = dataInativacao
    )