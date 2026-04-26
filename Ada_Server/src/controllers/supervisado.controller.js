import { supabase } from "../config/supabase.js"

//===========================================Equipo===================================================

//crear equipo
export const crearEquipoComputo = async (req, res) => {

    try {

        const { id } = req.params;
        console.log("El id es: " + id);

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido'
            })
        }

        //Validacion de que existe el supervisado
        const { data: supervisado } = await supabase
            .from('supervisado')
            .select('id')
            .eq('id', id)
            .single();

        if (!supervisado) {
            return res.status(404).json({
                error: 'supervisado no existe'
            });
        }

        const {
            nombre,
            device_uuid
        } = req.body;

        //Insertar Datos
        const { data, error } = await supabase
            .from('equipo')
            .insert([
                {
                    supervisado_id: id,
                    nombre,
                    device_uuid
                }
            ])
            .select();

        if (error) {
            //Errores de supaBase
            return res.status(400).json({
                error: error.message
            });
        }

        res.status(201).json({
            message: `Equipo del supervisado ${id} Registrado`,
            data
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
}

//borrar equipo
export const eliminarEquipoComputo = async (req, res) => {

    try {

        const { id, idEquipo } = req.params;

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido del supervisado'
            });
        }

        if (!idEquipo) {
            return res.status(400).json({
                error: 'ID es requerido el ID del equipo'
            });
        }

        //Validar que el supervisado pertenece al supervisor
        const { data: equipo, error: findError } = await supabase
            .from('equipo')
            .select('*')
            .eq('id', idEquipo)
            .eq('supervisado_id', id)
            .single();

        if (findError || !equipo) {
            return res.status(404).json({
                error: 'Equipo no encontrado o no pertenece al supervisado'
            });
        }

        //Eliminar
        const { error } = await supabase
            .from('equipo')
            .delete()
            .eq('id', idEquipo);

        if (error) {
            return res.status(400).json({
                error: error.message
            });
        }

        res.json({
            message: 'Equipo eliminado'
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }

}

//Consultar Equipos de un supervisado
export const consultarEquipoComputo = async (req, res) => {
    try {
        const { id } = req.params; // id del supervisado

        if (!id) {
            return res.status(400).json({
                error: 'ID del supervisado requerido'
            });
        }

        const { data, error } = await supabase
            .from('equipo')
            .select('*')
            .eq('supervisado_id', id);

        if (error) {
            return res.status(400).json({
                error: error.message
            });
        }

        res.json({
            total: data.length,
            data
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
}

//Crear Alertas
export const crearAlerta = async (req, res) => {
    try {
        const { id, idEquipo, idAplicacion } = req.params;
        console.log("El id es: " + id);

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido'
            })
        }

        //Validacion de que existe el supervisado
        const { data: supervisado } = await supabase
            .from('supervisado')
            .select('id')
            .eq('id', id)
            .single();

        if (!supervisado) {
            return res.status(404).json({
                error: 'supervisado no existe'
            });
        }


        //Validacion de que existe el equipo
        const { data: equipo } = await supabase
            .from('equipo')
            .select('id')
            .eq('id', idEquipo)
            .single();

        if (!equipo) {
            return res.status(404).json({
                error: 'equipo no existe'
            });
        }

        const {
            aplicacion_id,
            tipo_alerta,
            mensaje_detectado,
            lat,
            lon,
            colonia,
            municipio,
            estado

        } = req.body;

        //Insertar Datos
        const { data, error } = await supabase
            .from('alerta')
            .insert([
                {
                    equipo_id: idEquipo,
                    aplicacion_id,
                    tipo_alerta,
                    mensaje_detectado,
                    lat,
                    lon,
                    colonia,
                    municipio,
                    estado
                }
            ])
            .select();

        if (error) {
            //Errores de supaBase
            return res.status(400).json({
                error: error.message
            });
        }

        res.status(201).json({
            message: `Alerta del equipo ${idEquipo} Registrada`,
            data
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
}

//Crear App
export const crearApp = async (req, res) => {
    try {
        const { id } = req.params;
        console.log("El id es: " + id);

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido'
            })
        }

        //Validacion de que existe el supervisado
        const { data: supervisado } = await supabase
            .from('supervisado')
            .select('id')
            .eq('id', id)
            .single();

        if (!supervisado) {
            return res.status(404).json({
                error: 'supervisado no existe'
            });
        }

        const {
            nombre,
            package_name
        } = req.body;

        //Insertar Datos
        const { data, error } = await supabase
            .from('aplicacion')
            .insert([
                {
                    nombre,
                    package_name
                }
            ])
            .select();

        if (error) {
            //Errores de supaBase
            return res.status(400).json({
                error: error.message
            });
        }

        res.status(201).json({
            message: `aplicacion ${id} Registrada`,
            data
        });


    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
}

export const crearVinculacion = async (req, res) => {
    try {

        const { id } = req.params;
        console.log("El id es: " + id);

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido'
            })
        }

        //Insertar Datos
        const { data, error } = await supabase
            .from('codigoVinculacion')
            .insert([
                {
                    id_Supervisado: id,
                    expiracion: new Date(Date.now() + 10 * 60 * 1000),
                    usado: false,
                    codigo: Math.random().toString(36).substring(2, 8).toUpperCase(),
                }
            ])
            .select();

        if (error) {
            //Errores de supaBase
            return res.status(400).json({
                error: error.message
            });
        }

        res.status(201).json({
            message: `codigo Registrad`,
            codigo: data[0].codigo
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
}