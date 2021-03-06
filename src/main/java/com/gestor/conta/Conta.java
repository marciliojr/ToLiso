package com.gestor.conta;

import com.gestor.banco.Banco;
import com.gestor.despesa.Despesa;
import com.gestor.receita.Receita;
import com.gestor.usuario.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Conta extends PanacheEntity implements Serializable {
    @OneToOne
    public Banco banco;
    @OneToOne
    public Usuario usuario;
    public BigDecimal saldo;
    @OneToMany(fetch = FetchType.LAZY)
    public List<Despesa> despesa;
    @OneToMany(fetch = FetchType.LAZY)
    public List<Receita> receita;


    public static Conta buscarPorBanco(Integer codigoBanco){
        Map<String, Object> params = new HashMap<>();
        params.put("codigoBanco", codigoBanco);
        return Conta.find("banco.numero = :codigoBanco ", params).firstResult();
    }

    public static BigDecimal getSaldoRealConta(Integer codigoBanco, String emailUsuario) {

        Map<String, Object> params = new HashMap<>();
        params.put("usuarioEmail", emailUsuario);
        params.put("codigoBanco", codigoBanco);

        Conta conta = Conta.find("banco.numero = :codigoBanco AND usuario.email = :usuarioEmail", params).firstResult();
        BigDecimal saldo = conta.saldo;

        BigDecimal valor = BigDecimal.ZERO;
        BigDecimal resposta = BigDecimal.ZERO;
        List<Despesa> despesas = conta.despesa;

        for (Despesa d: despesas) {
            resposta = valor.add(d.valor);
        }

        return saldo.subtract(resposta);
    }

    @Override
    public String toString() {
        return "Conta{" +
                "banco=" + banco +
                ", usuario=" + usuario +
                ", saldo=" + saldo +
                '}';
    }
}
