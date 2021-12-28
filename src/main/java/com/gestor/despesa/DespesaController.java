package com.gestor.despesa;

import com.gestor.despesa.dto.DespesaDTO;
import com.gestor.despesa.dto.ListaResponseDespesaDTO;
import com.gestor.receita.ReceitaService;
import com.gestor.util.dto.BigDecimalDTO;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/despesa")
public class DespesaController {

    @Inject
    private DespesaService service;

    @GET
    @Path("saldoDespesaMes/{usuarioEmail}")
    public Response obterSaldoGeralContas(@PathParam String usuarioEmail) {
        BigDecimalDTO resposta = service.obterSaldoDespesasMes(usuarioEmail);
        return Response.ok(resposta).build();
    }

    @GET
    @Path("despesaMes/{usuarioEmail}")
    public Response obterDespesasMes(@PathParam String usuarioEmail) {
        ListaResponseDespesaDTO resposta = service.obterDespesasMes(usuarioEmail);
        return Response.ok(resposta).build();
    }


}
