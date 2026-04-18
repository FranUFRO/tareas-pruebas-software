package com.tarea_property_based_testing.spring_boot;

import tools.jackson.databind.ObjectMapper;
import com.tarea_property_based_testing.spring_boot.usuario.entity.Usuario;
import com.tarea_property_based_testing.spring_boot.usuario.repository.UsuarioRepository;
import net.jqwik.api.*;
import net.jqwik.api.lifecycle.AfterContainer;
import net.jqwik.api.lifecycle.BeforeContainer;
import net.jqwik.api.lifecycle.BeforeTry;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UsuarioApiPropertyTest {

    private static ConfigurableApplicationContext contexto;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UsuarioRepository usuarioRepository;

    @BeforeContainer
    static void levantarAplicacion() {
        contexto = new SpringApplicationBuilder(Application.class)
                .properties(
                        "spring.datasource.url=jdbc:h2:mem:testdb",
                        "spring.datasource.driver-class-name=org.h2.Driver",
                        "spring.datasource.username=sa",
                        "spring.datasource.password=",
                        "spring.jpa.hibernate.ddl-auto=create-drop",
                        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                        "server.port=0"
                )
                .run();
    }

    @AfterContainer
    static void cerrarAplicacion() {
        if (contexto != null) {
            contexto.close();
        }
    }

    @BeforeTry
    void inicializar() {
        WebApplicationContext webContext = (WebApplicationContext) contexto;
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        objectMapper = contexto.getBean(ObjectMapper.class);
        usuarioRepository = contexto.getBean(UsuarioRepository.class);
        usuarioRepository.deleteAll();
    }

    @Provide
    Arbitrary<String> nombresValidos() {
        return Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> emailsValidos() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .ofMinLength(5).ofMaxLength(15)
                .map(s -> s + "@test.com");
    }

    @Provide
    Arbitrary<String> contraseñasValidas() {
        return Arbitraries.strings()
                .withCharRange('a', 'z')
                .withCharRange('A', 'Z')
                .withCharRange('0', '9')
                .ofMinLength(8)
                .ofMaxLength(30);
    }

    @Provide
    Arbitrary<Usuario> usuariosNuevos() {
        return Combinators.combine(nombresValidos(), emailsValidos(), contraseñasValidas())
                .as((nombre, email, contraseña) -> new Usuario(null, nombre, email, contraseña));
    }

    @Property(tries = 20)
    void crearUsuario(
        @ForAll("usuariosNuevos") Usuario usuario) throws Exception {
                String json = objectMapper.writeValueAsString(usuario);
                mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value(usuario.getNombre()))
                .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                .andExpect(jsonPath("$.contraseña").value(usuario.getContraseña()))
                .andExpect(result -> {
                        String response = result.getResponse().getContentAsString();
                        Usuario creado = objectMapper.readValue(response, Usuario.class);
                        assert usuarioRepository.existsById(creado.getId());
                });
        }

    @Property(tries = 20)
    void obtenerUsuarioPorId(
        @ForAll("usuariosNuevos") Usuario usuario) throws Exception {
                String jsonCrear = objectMapper.writeValueAsString(usuario);
                MvcResult resultadoCrear = mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCrear))
                .andExpect(status().isCreated())
                .andReturn();
                Usuario creado = objectMapper.readValue(
                        resultadoCrear.getResponse().getContentAsString(), Usuario.class);
                Long id = creado.getId();
                mockMvc.perform(get("/api/usuarios/{id}", id))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(id))
                        .andExpect(jsonPath("$.nombre").value(usuario.getNombre()))
                        .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                        .andExpect(jsonPath("$.contraseña").value(usuario.getContraseña()));
        }

    @Property(tries = 20)
    void actualizarNombreDeUsuario(
            @ForAll("usuariosNuevos") Usuario usuario,
            @ForAll("nombresValidos") String nuevoNombre) throws Exception {
        String jsonCrear = objectMapper.writeValueAsString(usuario);
        MvcResult resultadoCrear = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCrear))
                .andExpect(status().isCreated())
                .andReturn();

        Usuario creado = objectMapper.readValue(
                resultadoCrear.getResponse().getContentAsString(), Usuario.class);
        Long id = creado.getId();
        Usuario datosActualizar = new Usuario(null, nuevoNombre, null, null);
        String jsonActualizar = objectMapper.writeValueAsString(datosActualizar);

        mockMvc.perform(put("/api/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonActualizar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value(nuevoNombre))
                .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                .andExpect(jsonPath("$.contraseña").value(usuario.getContraseña()));
    }

    @Property(tries = 20)
    void eliminarUsuarioExistente(
            @ForAll("usuariosNuevos") Usuario usuario) throws Exception {
        String jsonCrear = objectMapper.writeValueAsString(usuario);
        MvcResult resultadoCrear = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCrear))
                .andExpect(status().isCreated())
                .andReturn();

        Usuario creado = objectMapper.readValue(
                resultadoCrear.getResponse().getContentAsString(), Usuario.class);
        Long id = creado.getId();
        mockMvc.perform(delete("/api/usuarios/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/usuarios/{id}", id))
                .andExpect(status().isNotFound());
    }
}
