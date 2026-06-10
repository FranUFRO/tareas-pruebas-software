import { Injectable } from '@nestjs/common';
import axios from 'axios';
import { ConfigService } from '@nestjs/config';
import { CreatePlanDto } from './dto/create_plan.dto';

@Injectable()
export class OpenrouterService {
  constructor(private readonly configService: ConfigService) {}

  hasApiKey(): boolean {
    return !!this.configService.get<string>('OPENROUTER_API_KEY');
  }

  async generatePlan(dto: CreatePlanDto) {
    if (!this.hasApiKey()) {
      return this.generateLocalPlan(dto);
    }

    try {
      const response = await axios.post(
        'https://openrouter.ai/api/v1/chat/completions',
        {
          model:
            this.configService.get<string>('OPENROUTER_MODEL'),
          messages: [
            {
              role: 'system',
              content:
                'Eres un asistente que genera planes de estudio. Responde solamente JSON válido.',
            },
            {
              role: 'user',
              content: this.buildPrompt(dto),
            },
          ],
        },
        {
          headers: {
            Authorization: `Bearer ${this.configService.get<string>(
              'OPENROUTER_API_KEY',
            )}`,
            'Content-Type': 'application/json',
          },
        },
      );

      const content = response.data.choices[0].message.content;
      const parsed = JSON.parse(content);

      return parsed.weeks;
    } catch {
      return this.generateLocalPlan(dto);
    }
  }

  private buildPrompt(dto: CreatePlanDto): string {
    return `
Genera un plan de estudio en JSON válido.

Temas:
${dto.topics.join(', ')}

Cantidad de semanas:
${dto.weeks}

Horas por semana:
${dto.hoursPerWeek}

Fecha de inicio:
${dto.startDate || 'No indicada'}

Restricciones:
${dto.restrictions?.join(', ') || 'Sin restricciones'}

Formato obligatorio:
{
  "weeks": [
    {
      "week": 1,
      "topics": ["tema"],
      "objective": "objetivo de la semana",
      "activities": ["actividad 1", "actividad 2"],
      "estimatedHours": 5
    }
  ]
}

Reglas:
- La cantidad de semanas debe ser exactamente ${dto.weeks}.
- Todos los temas deben aparecer al menos una vez.
- Cada semana debe tener actividades no vacías.
- estimatedHours no debe superar ${dto.hoursPerWeek}.
- Debe existir al menos una actividad de repaso o evaluación.
`;
  }

  private generateLocalPlan(dto: CreatePlanDto) {
    const weeks: Array<{
      week: number;
      topics: string[];
      objective: string;
      activities: string[];
      estimatedHours: number;
    }> = [];

    for (let i = 1; i <= dto.weeks; i++) {
      const topic = dto.topics[(i - 1) % dto.topics.length];

      weeks.push({
        week: i,
        topics: [topic],
        objective: `Comprender y practicar ${topic}`,
        activities: [
          `Estudiar contenidos de ${topic}`,
          `Resolver ejercicios de ${topic}`,
        ],
        estimatedHours: dto.hoursPerWeek,
      });
    }

    weeks[dto.weeks - 1].activities.push('Repaso general y evaluación final');

    return weeks;
  }
}