window.onload = inicializa;

var Persona = function (nombre, apellidos) {
    this.nombre = nombre;
    this.apellidos = apellidos;

    this.toString = function () {
        return ["Hola, soy ", this.nombre, " ", this.apellidos].join("");
    };
};

function saludar() {

    var persona = new Persona(document.getElementById("nombre").value, document.getElementById("apellidos").value);
    alert(persona.toString());
}

function inicializa() {
    //Anhadir evento click al boton Pulsar y que cambie el texto
    document.getElementById("btnSaludar").addEventListener("click", saludar, false);
}