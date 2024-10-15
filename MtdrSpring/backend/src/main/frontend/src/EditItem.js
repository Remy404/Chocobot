import React, { useState } from "react";
import Button from '@mui/material/Button';

function EditItem(props) {
    // Estados para los tres campos: descripción, storypoints, responsable
    const [newDescription, setNewDescription] = useState(props.description);
    const [newStorypoints, setNewStorypoints] = useState(props.storypoints);
    const [newResponsable, setNewResponsable] = useState(props.responsable);

    const handleEdit = (e) => {
        e.preventDefault();

        // Envía todos los datos actualizados: descripción, storypoints y responsable
        props.updateItem(props.id, {
            description: newDescription,
            storypoints: newStorypoints,
            responsable: newResponsable,
        });

        // Cierra el formulario después de la edición
        props.onClose();
    };

    return (
        <div>
            <form onSubmit={handleEdit}>
                <input
                    type="text"
                    value={newDescription}
                    onChange={(e) => setNewDescription(e.target.value)}
                    placeholder="Nueva descripción"
                />
                <input
                    type="number"
                    value={newStorypoints}
                    onChange={(e) => setNewStorypoints(e.target.value)}
                    placeholder="Storypoints"
                />
                <input
                    type="text"
                    value={newResponsable}
                    onChange={(e) => setNewResponsable(e.target.value)}
                    placeholder="Responsable"
                />
                <Button type="submit" variant="contained">Guardar</Button>
                <Button onClick={props.onClose} variant="outlined">Cancelar</Button>
            </form>
        </div>
    );
}

export default EditItem;
