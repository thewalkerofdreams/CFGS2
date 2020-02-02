//import myModule from  "ClsPersona.js";

window.onload = inicializa;//Sin parentesis, sino obligamos a ejecutar del tirón la función

class ClsPersona {
    constructor(nombre, apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }
} 

function toString() {//Que es esto de tener que tener los métodos fuera de la clase...
    return "nombre --> " + nombre.value + ", apellidos --> " + apellidos.value;
}

//Función que inicializa los elementos de la página
function inicializa() {
    document.getElementById("btnEnviar").addEventListener("click", mostrarPersona);
}

//Este función nos permite mostrar por pantalla el nombre y apellidos de la persona
function mostrarPersona() {
    if (document.getElementById("nombre").value != "" && document.getElementById("apellidos").value != "") {
        var persona = new ClsPersona(document.getElementById("nombre").value, document.getElementById("apellidos").value);
        alert(toString());
    }
}