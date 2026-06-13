package br.com.edificiopromenade.data.local.relation

import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.data.local.entity.MoradorEntity

data class ApartamentoDetalhadoCompleto(

    val apartamento: ApartamentoEntity,

    val condominio: CondominioEntity?,

    val moradores: List<MoradorEntity>
)