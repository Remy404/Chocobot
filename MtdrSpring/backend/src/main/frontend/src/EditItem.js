// EditItem.js

import React, { useState } from "react";
import Button from '@mui/material/Button';

function EditItem(props) {
    const [newDescription, setNewDescription] = useState(props.description);

    const handleEdit = (e) => {
        e.preventDefault();
        props.updateItem(props.id, newDescription);
        props.onClose(); // Cierra el formulario después de editar
    };

    return (
        <div>
            <form onSubmit={handleEdit}>
                <input
                    type="text"
                    value={newDescription}
                    onChange={(e) => setNewDescription(e.target.value)}
                />
                <Button type="submit" variant="contained">Guardar</Button>
                <Button onClick={props.onClose} variant="outlined">Cancelar</Button>
            </form>
        </div>
    );
}

export default EditItem;
