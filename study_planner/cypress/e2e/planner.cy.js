describe('Pruebas de humo - Planner API', () => {
  const baseUrl = 'http://localhost:3000';

  it('GET /planner/status responde 200 e informa conexión LLM', () => {
    cy.request(`${baseUrl}/planner/status`).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body.status).to.eq('ok');
      expect(response.body.llm).to.eq('configured');
    });
  });

  it('POST /planner rechaza solicitud inválida', () => {
    cy.request({
      method: 'POST',
      url: `${baseUrl}/planner`,
      failOnStatusCode: false,
      body: {},
    }).then((response) => {
      expect(response.status).to.eq(400);
      expect(response.body.message).to.exist;
    });
  });

  it('POST /planner genera un plan coherente', () => {
    const body = {
      topics: ['POO', 'Patrones de diseño', 'Testing'],
      weeks: 4,
      hoursPerWeek: 8,
    };

    cy.request({
      method: 'POST',
      url: `${baseUrl}/planner`,
      body,
    }).then((response) => {
      expect(response.status).to.be.oneOf([200, 201]);

      expect(response.body.generatedBy).to.eq('llm');
      expect(response.body.generatedAt).to.exist;
      expect(response.body.weeks).to.have.length(body.weeks);

      const plannedTopics = response.body.weeks.flatMap((w) => w.topics);

      body.topics.forEach((topic) => {
        expect(plannedTopics).to.include(topic);
      });

      response.body.weeks.forEach((week) => {
        expect(week.activities).to.not.be.empty;
        expect(week.estimatedHours).to.be.at.most(body.hoursPerWeek);
      });

      const hasReview = response.body.weeks.some((week) =>
        week.activities.some((activity) => {
          const text = activity.toLowerCase();
          return (
            text.includes('repaso') ||
            text.includes('evaluación') ||
            text.includes('evaluacion')
          );
        }),
      );

      expect(hasReview).to.eq(true);
    });
  });
});