package com.gestor.usuario;

import com.gestor.exceptions.NegocioException;
import com.gestor.exceptions.NegocioExceptionDTO;
import com.gestor.usuario.dto.UsuarioDTO;
import com.gestor.util.CriptografiaUtil;
import com.gestor.util.MensagemErro;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Validator;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

@RequestScoped
public class UsuarioService {

    @Inject
    private UsuarioRepository repositorio;

    @Inject
    private Validator validador;

    @Transactional
    public void insere(UsuarioDTO usuario) throws NoSuchAlgorithmException, UnsupportedEncodingException, NegocioException {

        validarObjeto(usuario);

        if (repositorio.existeUsuarioComEmail(usuario.getEmail())) {
            throw new NegocioException(MensagemErro.MENSAGEM_EMAIL_JA_CADASTRADO);
        }

        String senhaCriptografada = CriptografiaUtil.criptografarSenhaUsuario(usuario.getSenha());

        Usuario usuarioBase = new Usuario(usuario.getEmail(), usuario.getNick(), senhaCriptografada);

        repositorio.persist(usuarioBase);
    }

    private void validarObjeto(UsuarioDTO usuario) throws NegocioException {

        NegocioExceptionDTO negocioExceptionDTO = new NegocioExceptionDTO();
        validador.validate(usuario).forEach(usuarioValidado -> {
            String propriedade = usuarioValidado.getPropertyPath().toString();
            String mensagem = usuarioValidado.getMessage();
            String mensagemFormatada = MessageFormat.format("Campo {0} {1} ", propriedade, mensagem);
            negocioExceptionDTO.addMensagem(mensagemFormatada);
        });

        if (negocioExceptionDTO.comMensagens()) {
            throw new NegocioException(negocioExceptionDTO.toString());
        }
    }


    public Usuario buscarPorEmail(String email) {
        return repositorio.buscarUsuarioPorEmail(email);
    }


}
