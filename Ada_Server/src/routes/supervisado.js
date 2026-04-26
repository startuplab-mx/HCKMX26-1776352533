import express from 'express'
import {
    crearEquipoComputo,
    eliminarEquipoComputo,
    consultarEquipoComputo,
    crearAlerta,
    crearApp,
    crearVinculacion
} from '../controllers/supervisado.controller.js'

const router = express.Router();

//CREAR EQUIPO DE COMPUTO
router.post('/equipo/crear/:id', crearEquipoComputo);

//ELIMINAR EQUIPO DE COMPUTO
router.post('/equipo/eliminar/:id/:idEquipo', eliminarEquipoComputo);

//CONSULTAR EQUIPO DE COMPUTO
router.get('/equipo/:id', consultarEquipoComputo);

//CREAR ALERTA
router.post('/alerta/crear/:id/:idEquipo', crearAlerta);

//CREAR APLICACION
router.post('/aplicacion/crear/:id', crearApp);

//Crear Codigo de vinculacion
router.post('/codigo/vinculacion/:id', crearVinculacion);

export const path = '/supervisado';
export default router;