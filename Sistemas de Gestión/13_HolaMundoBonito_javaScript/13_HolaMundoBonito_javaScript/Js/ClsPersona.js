class ClsPersona{
    constructor(nombre, apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }
} 

function toString() {
    return "nombre --> " + nombre + ", apellidos --> " + apellidos;
}

module.exports.ClsPersona = ClsPersona;