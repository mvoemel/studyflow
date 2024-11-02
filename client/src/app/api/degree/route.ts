import { NextRequest, NextResponse } from 'next/server';

export async function GET(req: NextRequest) {
    const cookie = req.cookies.get('selectedDegree');
    const selectedDegree = cookie ? cookie.value : '';
    return NextResponse.json({ selectedDegree });
}

export async function POST(req: NextRequest) {
    const { degree } = await req.json();
    const response = NextResponse.json({ message: 'Degree set' });
    response.cookies.set('selectedDegree', degree, { path: '/' });
    return response;
}