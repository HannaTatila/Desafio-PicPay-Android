package com.hanna.picpaydesafio.apresentacao.pagamento

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.cartao.CadastroCartaoActivity
import com.hanna.picpaydesafio.dados.ConstantesPersistencia
import com.hanna.picpaydesafio.dados.PreferenciasSeguranca
import kotlinx.android.synthetic.main.activity_pagamento.*
import kotlinx.android.synthetic.main.view_recibo.view.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*


class PagamentoActivity : AppCompatActivity() {

    private lateinit var mContatoPreferencias: PreferenciasSeguranca
    private lateinit var mPagamentoViewModel: PagamentoViewModel
    private var mNumeroProtegido: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)

        mContatoPreferencias = PreferenciasSeguranca(this)

        configuraNumeroCartao()
        manipulaPagamentoViewModel()
        capturaEventoAtualizacaoValor()
        controlaEventoClique()
    }

    private fun controlaEventoClique() {
        botao_voltar_pagamento.setOnClickListener { onBackPressed() }

        link_editar_cartao.setOnClickListener { chamaTelaCadastro() }

        button_pagar.setOnClickListener {
            //val valorPagamento = currencyedit_valor.text.toString().toDouble() TODO: transformar em BigDecimal depois
            val valorPagamento: Double = 5.52 //TODO: erro ao passar o valor Double
            mPagamentoViewModel.enviaDadosTransacao(this, valorPagamento)
        }
    }

    private fun chamaTelaCadastro() {
        // TODO: Mandar numero do cartao. Se tiver preenchido é atualização, senão é inserção
        val intent = CadastroCartaoActivity.buscaIntent(this)
        this.startActivity(intent)
    }

    private fun manipulaPagamentoViewModel() {
        mPagamentoViewModel = ViewModelProviders.of(this).get(PagamentoViewModel::class.java).also {
            it.buscaDadosRecebedor(this)
            recebedorObserver(it)
            transacaoObserver(it)
        }
    }

    private fun recebedorObserver(mPagamentoViewModel: PagamentoViewModel) {
        mPagamentoViewModel.dadosRecebedorLiveData.observe(this, Observer {
            it?.let { dadosRecebedor ->
                var urlImagem: String? = dadosRecebedor[ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO]
                Glide.with(this@PagamentoActivity).load(urlImagem).into(image_foto_contato)
                text_username_contato.text = dadosRecebedor[ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO]
                text_numero_cartao.text = "Mastercard $mNumeroProtegido •"
            }
        })
    }

    private fun transacaoObserver(mPagamentoViewModel: PagamentoViewModel) {
        mPagamentoViewModel.transacaoLiveData.observe(this, Observer {
            it?.let { transacao ->
                if (transacao.sucesso) {
                    toast(getString(R.string.mag_sucesso_pagamento))
                    val viewRecibo = criaViewRecibo()
                    geraRecibo(viewRecibo, transacao.recebedor.imagem, transacao.recebedor.username, transacao.id)
                } else {
                    toast(getString(R.string.mag_recusa_pagamento))
                }
            }
        })
    }

    private fun configuraNumeroCartao() {
        val pacote = intent.extras
        if (pacote != null) {
            val numeroCartao = pacote.getString("NUMERO_CARTAO") //TODO: criar constante
            mNumeroProtegido = numeroCartao?.toString()!!.take(4)
        }
    }

    private fun capturaEventoAtualizacaoValor() {
        currencyedit_valor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val conteudoCampoValor = currencyedit_valor.text.toString().trim()
                val valorFoiPreenchido = conteudoCampoValor.isNotEmpty() && conteudoCampoValor != "0,00"
                if (valorFoiPreenchido) customizaTela(R.color.colorAccent) else customizaTela(R.color.corCinzaBotaoDesabilitado)
                button_pagar.isEnabled = valorFoiPreenchido
            }
        })
    }

    private fun customizaTela(corTexto: Int) {
        val cor = ContextCompat.getColor(this@PagamentoActivity, corTexto)
        currencyedit_valor.setTextColor(cor)
        txt_cifrao.setTextColor(cor)
    }

    fun criaViewRecibo(): View {
        return this.layoutInflater.inflate(R.layout.view_recibo, null)
    }

    @SuppressLint("SetTextI18n")
    private fun geraRecibo(view: View, imagem: String, username: String, idTransacao: Int) {
        Glide.with(view.context).load(imagem).into(view.img_foto_contato)
        view.txt_username_contato.text = username
        view.txt_numero_transacao.text = getString(R.string.transacao) + idTransacao.toString()

        val dataHoraAtual = buscaDataHoraAtual() //timestamp
        view.txt_data_hora.text = dataHoraAtual

        view.txt_dados_cartao.text = "Cartão Master $mNumeroProtegido"

        val caixaDialogo = BottomSheetDialog(this)
        caixaDialogo.setContentView(view)
        caixaDialogo.show()
    }

    private fun buscaDataHoraAtual(): String { //timestamp: String): String {
        val formatoDataHora = SimpleDateFormat("dd/MM/yyyy 'às' hh:mm", Locale("pt", "BR"))
        val calendario = Calendar.getInstance()

        val ano = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val diaDoMes = calendario.get(Calendar.DAY_OF_MONTH)
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minuto = calendario.get(Calendar.MINUTE)

        calendario.set(ano, mes, diaDoMes, hora, minuto)
        println(calendario.time)

        return formatoDataHora.format(calendario.time)

        /*
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = timestamp.toLong()
        val formatoDataHora = SimpleDateFormat("dd-MM-yyyy 'àsss' hh:mm", Locale.getDefault())
        return formatoDataHora.format(calendario.timeInMillis)
        */
        //val date = DateFormat.format("dd-MM-yyyy 'às' hh:mm:ss", calendario).toString()
    }


    companion object {
        private const val NUMERO_CARTAO = "NUMERO_CARTAO" //TODO: criar constante

        fun buscaIntent(contexto: Context, numeroCartao: String): Intent {
            val pacote = Bundle()
            pacote.putString(NUMERO_CARTAO, numeroCartao)
            return Intent(contexto, PagamentoActivity::class.java).putExtras(pacote)
        }
    }
}
