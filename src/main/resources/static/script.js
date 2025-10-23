const API_URL = "http://localhost:8080/alimentos";

document.getElementById("alimentoForm").addEventListener("submit", async function (event) {
        event.preventDefault();
        const nomeAlimento = document.getElementById("name").value;
        const codigoDeBarras = document.getElementById("codBarras").value;
        const preco = document.getElementById("preco").value;
        const dataFabricacao = document.getElementById("fabricacao").value;
        const dataValidade = document.getElementById("validade").value;
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ codigoDeBarras , nomeAlimento, preco, dataFabricacao, dataValidade })
        }).catch(err => alert(err));
        if (response.ok) {
            document.getElementById("alimentoForm").reset();
            carregarAlimentos();
        }
    });

async function carregarAlimentos() {
    const response = await fetch(API_URL);

    const alimentos = await response.json();
    const alimentoTable = document.getElementById("alimentoTable");
    alimentoTable.innerHTML = "";
    alimentos.forEach(alimento => {
        const row =
            `<tr><td>${alimento.id}</td><td>${alimento.codigoDeBarras}</td><td>${alimento.nomeAlimento}</td><td>${alimento.preco}</td><td>${alimento.dataFabricacao}</td><td>${alimento.dataValidade}</td></tr>`;
        alimentoTable.innerHTML += row;
    });
}

carregarAlimentos();