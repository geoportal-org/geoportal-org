import { NextResponse } from 'next/server';

export async function GET(request) {
    return NextResponse.json({ url: process.env.NEXT_PUBLIC_MATOMO_URL });
}