import { NextApiRequest, NextApiResponse } from 'next';

// Mock data, replace with actual database query
const events = [
    { title: 'Höhere Mathematik 1', start: '2024-10-10T12:30:00', end: '2024-10-10T18:00:00' },
    { title: 'Software Entwicklung 1', start: '2024-10-10T08:00:00', end: '2024-10-10T12:00:00' }
];

export default function handler(req: NextApiRequest, res: NextApiResponse) {
    res.status(200).json(events);
}