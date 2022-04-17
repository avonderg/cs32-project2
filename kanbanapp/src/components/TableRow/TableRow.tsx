import React, {useState} from 'react';
import KanbanCell from './KanbanCell';

type TableRowProps = {
    kanban_status: string[];
    kanban_text: string[][];
};

function TableRow(props: TableRowProps) {
    console.log(props)
    return(

        <tr>
            {/* {props.kanban_text.map(item => <KanbanCell header={props.vals[item]} />)} */}
        </tr>
    )
}

export default TableRow;