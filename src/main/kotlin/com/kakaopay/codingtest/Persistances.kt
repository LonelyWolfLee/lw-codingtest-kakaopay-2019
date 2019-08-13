package com.kakaopay.codingtest

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

@Entity
data class Institute(
        @Column(name = "institute_code")
        val code: String,
        @Column(name = "institute_name")
        val name: String,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)

interface InstituteRepository: JpaRepository<Institute, Long>

@Entity
@Table(
        name = "finance_data"
)
data class FinanceData(
        val year: Int,
        val month: Int,
        @Column(name = "institute_code")
        val code: String,
        val amount: Int,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)

interface FinanceDataRepository: JpaRepository<FinanceData, Long> {
        fun findAllByCode(code: String): List<FinanceData>
}

@Entity
@Table(
        name = "auth_user"
)
data class AuthUser(
        @Column(name="user_id", unique = true)
        val userId: String,
        @Convert(converter = StringCipherConverter::class)
        val password: String,
        var token: String,

        @CreationTimestamp
        @Column(nullable = false, name = "created_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        val createdAt: Date? = null,
        @UpdateTimestamp
        @Column(nullable = false, name = "updated_at", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
        val updatedAt: Date? = null,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)

interface AuthUserRepository: JpaRepository<AuthUser, Long> {
        fun findAuthUserByUserIdAndPassword(userId: String, password: String): AuthUser?
        fun findAuthUserByUserIdAndToken(userId: String, password: String): AuthUser?
}
