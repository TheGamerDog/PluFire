package ru.thegamerdog.plufire.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import java.util.UUID

object Users : IntIdTable() {
    val petId = integer("petId").nullable()
    val petColor = varchar("petColor", 255).default("")
    val gees = integer("gees").default(20000000)
    val banned = bool("banned").default(false)
}

class UserModel(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserModel>(Users)

    var petId by Users.petId
    var petColor by Users.petColor
    var gees by Users.gees
    var banned by Users.banned
    var petToken: UUID = UUID.randomUUID()
}