import { supabase } from "../config/supabase.js"

//======================================SUPERVISOR=========================================================================
//Crear supervisor
export const crearSupervisor = async (req, res) => {
    try {
        const {
            nombre,
            appat,
            apmat,
            email,
            contrasena,
            fecha_nacimiento,
            curp,
            ine
        } = req.body;

        //Validacion Por Back
        if (!nombre || !appat || !email || !contrasena || !fecha_nacimiento) {
            return res.status(400).json({
                error: 'Faltan campos obligatorios'
            });
        }

        //Insertar Datos
        const { data, error } = await supabase
            .from('supervisor')
            .insert([
                {
                    nombre,
                    appat,
                    apmat,
                    email,
                    contrasena,
                    fecha_nacimiento,
                    curp,
                    ine
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
            message: 'Supervisor registrado',
        });

    } catch (err) {
        //Errores del servidor
        res.status(500).json({
            error: err.message
        });
    }
};

//Modificar Supervisor
export const updateSupervisor = async (req, res) => {
    try {
        const { id } = req.params;

        const {
            nombre,
            appat,
            apmat,
            email,
            contrasena,
            fecha_nacimiento
        } = req.body;

        //Validacion Por Back
        if (!id) {
            return res.status(400).json({
                error: 'ID requerido | No estás logeado'
            });
        }

        //Actualizamos segun lo que nos den en android
        const updateData = {};

        if (nombre) updateData.nombre = nombre;
        if (appat) updateData.appat = appat;
        if (apmat) updateData.apmat = apmat;
        if (email) updateData.email = email.toLowerCase();
        if (contrasena) updateData.contrasena = contrasena;
        if (fecha_nacimiento) updateData.fecha_nacimiento = fecha_nacimiento;

        const { data, error } = await supabase
            .from('supervisor')
            .update(updateData)
            .eq('id', id)
            .select();

        if (error) {
            return res.status(400).json({
                error: error.message
            });
        }

        res.json({
            message: 'Supervisor actualizado',
            data
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
};

//Eliminar Supervisor
export const deleteSupervisor = async (req, res) => {
    try {
        const { id } = req.params;

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido'
            });
        }

        const { error } = await supabase
            .from('supervisor')
            .delete()
            .eq('id', id);

        if (error) {
            return res.status(400).json({
                error: error.message
            });
        }

        res.json({
            message: 'Supervisor eliminado'
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
};

//LoginSupervisor
export const loginSupervisor = async (req, res) => {
    try {
        const { email, contrasena } = req.body;

        if (!email || !contrasena) {
            return res.status(400).json({
                error: 'Email y contraseña requeridos'
            });
        }

        const { data, error } = await supabase
            .from('supervisor')
            .select('*')
            .eq('email', email.toLowerCase())
            .eq('contrasena', contrasena)
            .single();

        if (error || !data) {
            return res.status(401).json({
                error: 'Credenciales inválidas'
            });
        }

        res.json({
            message: 'Login exitoso',
            id: data.id
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
};

//Obtener Supervisor por ID
export const getSupervisorById = async (req, res) => {
    try {
        const { id } = req.params;
        console.log("El id es:" + id);

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido'
            });
        }

        const { data, error } = await supabase
            .from('supervisor')
            .select('*')
            .eq('id', id)
            .single();

        if (error || !data) {
            return res.status(404).json({
                error: 'Supervisor no encontrado'
            });
        }

        res.json(data);

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
};

//======================================SUPERVISADOS=========================================================================

//CRUD
//Crear Supervisado
export const crearSupervisado = async (req, res) => {
    try {

        const {
            nombre,
            appat,
            apmat,
            fecha_nacimiento,
            curp
        } = req.body;

        //Validacion Por Back
        if (!nombre || !appat || !fecha_nacimiento) {
            return res.status(400).json({
                error: 'Faltan campos obligatorios'
            });
        }

        //Insertar Datos
        const { data, error } = await supabase
            .from('supervisado')
            .insert([
                {
                    nombre,
                    appat,
                    apmat,
                    fecha_nacimiento,
                    curp
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
            message: 'Supervisado registrado',
            data
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
}

//Borrar Supervisado
export const borrarSupervisado = async (req, res) => {
    try {
        const { id, idSupervisado } = req.params;

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido del supervisor'
            });
        }

        if (!idSupervisado) {
            return res.status(400).json({
                error: 'ID requerido del supervisado'
            });
        }

        //Validar que el supervisado pertenece al supervisor
        const { data: supervisado, error: findError } = await supabase
            .from('supervisado')
            .select('*')
            .eq('id', idSupervisado)
            .eq('supervisor_id', id)
            .single();

        if (findError || !supervisado) {
            return res.status(404).json({
                error: 'Supervisado no encontrado o no pertenece al supervisor'
            });
        }

        //Eliminar
        const { error } = await supabase
            .from('supervisado')
            .delete()
            .eq('id', idSupervisado);

        if (error) {
            return res.status(400).json({
                error: error.message
            });
        }

        res.json({
            message: 'Supervisado eliminado'
        });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
};

//Actualizar Supervisado
export const updateSupervisado = async (req, res) => {

    try {
        const { id, idSupervisado } = req.params;

        if (!id) {
            return res.status(400).json({
                error: 'ID requerido del supervisor'
            });
        }

        if (!idSupervisado) {
            return res.status(400).json({
                error: 'ID requerido del supervisado'
            });
        }

        const {
            nombre,
            appat,
            apmat,
            fecha_nacimiento
        } = req.body;

        //Actualizamos segun lo que nos den en android
        const updateData = {};

        if (nombre) updateData.nombre = nombre;
        if (appat) updateData.appat = appat;
        if (apmat) updateData.apmat = apmat;
        if (fecha_nacimiento) updateData.fecha_nacimiento = fecha_nacimiento;

        const { data, error } = await supabase
            .from('supervisado')
            .update(updateData)
            .eq('id', idSupervisado)
            .select();

        if (error) {
            return res.status(400).json({
                error: error.message
            });
        }

        res.json({
            message: 'Supervisado actualizado',
            data
        });
    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
}

//Consultar Supervisados de un supervisor
export const consultarSupervisados = async (req, res) => {
    try {
        const { id } = req.params; // id del supervisor

        if (!id) {
            return res.status(400).json({
                error: 'ID del supervisor requerido'
            });
        }

        const { data, error } = await supabase
            .from('supervisado')
            .select('*')
            .eq('supervisor_id', id);

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

//Vincular Supervisados
export const vincularSupervisados = async (req, res) => {

    try {

        const { id } = req.params;

        if (!id) {
            return res.status(400).json({
                error: 'ID del supervisor requerido'
            });
        }

        const { error } = await supabase
            .from('supervisado')
            .select('*')
            .eq('supervisor_id', id);

        if (error) {
            return res.status(400).json({
                error: error.message
            });
        }

        const {
            codigo
        } = req.body;

        const { data} = await supabase
            .from('codigoVinculacion')
            .select('*')
            .eq('codigo', codigo)
            .single();

        if (!data || data.usado) {
            return res.status(400).json({ error: 'Código inválido' });
        }

        if (new Date(data.expira_en) < new Date()) {
            return res.status(400).json({ error: 'Código expirado' });
        }

        // Crear relación
        await supabase
            .from('supervisado')
            .update({ supervisor_id: id })
            .eq('id', data.id_Supervisado);

        res.json({ ok: true });

    } catch (err) {
        res.status(500).json({
            error: err.message
        });
    }
}