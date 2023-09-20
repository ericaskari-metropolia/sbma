import express from 'express';
import { nanoid } from 'nanoid';

const app = express()

app.use(express.urlencoded({ extended: false }));
app.use(express.json());

const data = new Map();

app.post('/', (req, res) => {
    const id = nanoid();
    const { body } = req;
    data.set(id, body ?? {});
    res.send({ id: id })
});

app.get('/:id', (req, res) => {
    const { id } = req.params;
    res.send({ data: data.get(id) ?? null })
});

app.listen(8000, '0.0.0.0', () => {
    console.log('Started listening on port 8000');
})
