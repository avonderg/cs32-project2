import React, {useState} from 'react';

type cell_props = {
    header: String;
    contents: String[];
}
function KanbanCell(props: cell_props) {
    return(
        <td className = "Cell">
            {props.contents}
        </td>
    );
}


export default KanbanCell;