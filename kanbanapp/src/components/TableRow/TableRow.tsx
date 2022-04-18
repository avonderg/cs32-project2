import React, {useState} from 'react';
import KanbanBox from '../KanbanBox/KanbanBox';


type TableValues = {
    text: string
}

function TableRow(props: TableValues) {

    return(
        <tr>
            <th>
                {props.text}
            </th>
        </tr>
    )
}

export default TableRow;